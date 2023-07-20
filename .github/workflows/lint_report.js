module.exports = async ({github, context, glob}) => {
    const globber = await glob.create('**/reports/*.xml')
    const files = await globber.glob()

    await github.rest.issues.createComment({
        issue_number: context.issue.number,
        owner: context.repo.owner,
        repo: context.repo.repo,
        body: '⚠️ Hi i found these xml files in the reports folder: \n files.join' + files.join('\n')
    })
}